## [Mars Rover kata](http://dallashackclub.com/rover) in Clojure using a finite state machine implemented with mutually recursive functions and trampoline.

The project uses [Midje](https://github.com/marick/Midje/).

### How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.
