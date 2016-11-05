# akka-persistence-journal-writer

[![Build Status](https://travis-ci.org/dnvriend/akka-persistence-jounal-writer.svg?branch=master)](https://travis-ci.org/dnvriend/akka-persistence-jounal-writer)
[ ![Download](https://api.bintray.com/packages/dnvriend/maven/akka-persistence-journal-writer/images/download.svg) ](https://bintray.com/dnvriend/maven/akka-persistence-journal-writer/_latestVersion)
[![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

__akka-persistence-query-writer__ consists of an akka-streams `Flow` and `Sink` that makes it possible to write
`EventEnvelope` , `Seq[EventEnvelope]`, `EventEnvelope2` or `Seq[EventEnvelope2]` to __any__ akka-persistence jounal.
It does this by sending messages directly to the [journal plugin itself](http://doc.akka.io/api/akka/2.4/#akka.persistence.journal.japi.AsyncWriteJournal).

# Documentation
- [akka-persistence-journal-writer docs](https://dnvriend.github.io/akka-persistence/index.html)