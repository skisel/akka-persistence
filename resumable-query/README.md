# akka-persistence-resumable-query

# Documentation
- [akka-persistence-resumable-query docs](https://dnvriend.github.io/akka-persistence/index.html)

## akka.persistence.query.extension.ResumableQuery
A helper component for `akka.persistence.query.scaladsl.ReadJournal` that stores the latest offset in
the akka-persistence journal so that the query can be resumed after being restarted from a previous offset.

The component only supports eventsByTag or eventsByPersistenceId for they return a stream of `EventEnvelope`.

### Configuration:
The resumable query component can be configured globally. This setting is used for all queries:

```
resumable-query {
  snapshot-interval = "250"
  backpressure-buffer = "1"
  journal-plugin-id = ""
  snapshot-plugin-id = ""
}
```

It supports configuring:

- snapshot-interval,
- backpressure-buffer,
- the journal plugin to use, when left empty, the default journal plugin will be used,
- the snapshot plugin to use, when left empty, the default snapshot plugin will be used.

### Query name
Each query should have an unique name, like for example `MessageReceivedEventQuery`, which will be used to write the offset and recover from it. The query name can also be used to configure a query individually. To configure the `MessageReceivedEventQuery`, the following configuration should be created:

```
MessageReceivedEventQuery {
  snapshot-interval = "250"
  backpressure-buffer = "1"
  journal-plugin-id = ""
  snapshot-plugin-id = ""
}
```

### Example usage:

```scala
import akka.stream.scaladsl.{ FileIO, Flow, Source }
import akka.persistence.query.extension.ResumableQuery
import akka.stream.integration.io._
import akka.stream.integration.xml.{ Validation, ValidationResult }

// for example, creating a never completing flow that resumes after restarting
// in this case I used it to send messages to a shard region
ResumableQuery("MessageReceivedEventQuery", offset ⇒ journal.eventsByTag(classOf[MessageReceivedEvent].getSimpleName, offset + 1))
  .join(fromProtoAs[MessageReceivedEvent].mapAsync(1) { msg =>
    messageReceivedRegion ? MessageReceived(msg.id, msg.foo, msg.bar)
  }).run()

// processing files and generating MD5 hashes and storing them in the journal using the
// akka.persistence.query.extension.Journal component
ResumableQuery("GenerateMD5Query", offset => journal.eventsByTag(classOf[GenerateMD5Command].getSimpleName, offset + 1))
  .join(fromProtoAs[GenerateMD5Command]
    .via(generateMD5Flow(baseDir))
    .via(Journal.flow())).run()

// generating MD5 is relatively easy using FileIO and the
// akka.stream.integration.io.DigestCalculator
def generateMD5Flow(implicit log: LoggingAdapter) = Flow[GenerateMD5Command].flatMapConcat {
 case GenerateMD5Command(fileName) =>
  FileIO.fromFile(new File(fileName)).via(DigestCalculator.hexString(Algorithm.MD5))
    .map(hash => MD5Generated(fileName, hash))
    .recover {
      case cause: Throwable =>
        MD5GeneratedFailed(fileName, cause.getMessage)
    }
}

// non-completing flow that resumes to validate XSD
ResumableQuery("ValidateXSDQuery", offset => journal.eventsByTag(classOf[ValidateFile].getSimpleName, offset + 1))
  .join(fromProtoAs[ValidateFile]
    .via(validateXSDFlow(baseDir)).via(Journal.flow())).run()

// processing files and validating XSD
def validateXSDFlow(implicit log: LoggingAdapter) = Flow[ValidateFile].flatMapConcat {
 case ValidateFile(fileName) =>
  Source.single(fileName).flatMapConcat { fileName =>
    FileIO.fromFile(new File(fileName)).via(Validation("/xsd/file.xsd")).map {
      case ValidationResult(Failure(cause)) =>
        ValidationFailed(fileName, cause.getMessage)
      case _: ValidationResult => Validated(fileName)
    }
  }.recover {
    case cause: Throwable =>
      ValidationFailed(fileName, cause.getClass.getName)
  }
}

// non-completing flow that moves files
ResumableQuery("MoveToDirQuery", offset => journal.eventsByTag(classOf[MoveToDirCmd].getSimpleName, offset + 1))
  .join(fromProtoAs[MoveToDirCmd]
    .via(moveToDirFlow).via(Journal.flow())).run()

def moveToHistoryFlow(implicit log: LoggingAdapter) = Flow[MoveToDirCmd].flatMapConcat {
 case MoveToDirCmd(from, to) =>
  Source.single(FileUtilsCommand.MoveFile(from, to))
    .via(FileUtils.moveFile).map {
      case MoveFileResult(Failure(cause)) =>
        FileMoveFailed(fileName, cause.getMessage)
      case _: MoveFileResult => FileMoved(from, to)
    }
}


import akka.NotUsed
import akka.persistence.query.EventEnvelope
import akka.stream.scaladsl.Flow
import com.google.protobuf.Message

// type classes to convert from/to protobuf
import com.google.protobuf.Message

trait ProtobufFormat[A] extends ProtobufReader[A] with ProtobufWriter[A]

trait ProtobufReader[A] {
  def read(proto: Message): A
}

trait ProtobufWriter[A] {
  def write(msg: A): Message
}

// converts from the data model to the domain model
object ProtobufAdapterFlow {
  def fromProtoAs[A: ProtobufReader]: Flow[EventEnvelope, A, NotUsed] =
    Flow[EventEnvelope]
      .collect {
        case EventEnvelope(_, _, _, proto: Message) =>
          implicitly[ProtobufReader[A]].read(proto)
      }
}
```