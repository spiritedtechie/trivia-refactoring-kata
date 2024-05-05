Trivia game refactoring kata
======

Refactored Trivia game codebase in Java, including addition of tests to support the refactoring.

It's slimmed down version of the original repository with only the Java version.

The idea of the exercise is to do some deliberate practice, and improve skills at designing test cases and refactoring. The idea is not to re-write the code from scratch, but rather to practice taking small steps, running the tests often, and incrementally improving the design.


## My notes

The Trivia game is a more challenging refactoring kata compared to Guilded Rose. I spent a several hours on it and observed a few things.

These core ideas still hold true - test/refactor small pieces, work towards the big, and avoid over-engineering. 

A key problem was the multiple data structures for the 'player' state. There was a clear need to encapsulate player state and behaviour.

It's tempting to create and test the entire Player class and then use it into the Game class. But that isn't very incremental.

Instead, several small refactorings were used to extract and test methods that eventually move to the Player object - things like 'getName' or 'updateNextPlace'.

The Strangler pattern was used to copy the state/behaviour to the Player object, test/switch to the new path, and finally remove the old state/behaviour from the Game class. This way, the Player class was incrementally built and incorporated, rather than in one big bang.

New classes were created where there was sufficient complexity i.e. player and question bank. In my opiono, aome solutions in the wild tend to go overboard with OOO.

Changing public interfaces to the Game class could break clients. Instead, I opted for a deprecation annotation for the old APIs and created a V2 of the APIs and a new game runner.

If you're curious, check out the commit history on GitHub for the complete story. 