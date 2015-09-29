# MarkovComposer

## Abstract
Algorithms, or algorithmic composition, have been used to compose music for centuries. For example, Western [punctus contra punctum](https://en.wikipedia.org/wiki/Counterpoint) can be sometimes reduced to algorithmic determinacy. Then, why not use fast-learning computers capable of billions of calculations per second to do what they do best, to follow algorithms?

## Markov chain?
[Markov chain](https://en.wikipedia.org/wiki/Markov_chain), named after Andrey Andreyevich Markov, is a (pseudo)random process of transition from one state to another. The transition is "memoryless" and it only depends on the current state and on the probabilities (saved in a so-called transition matrix). Sequence of events that preceeded the current state should in no way determine the transition. This "memorylessness" is also called Markov property. In short, transiting from one state to another is a random process based on probability.

## The general idea
Markov chain is just plain perfect for algorithmic music compositions. Notes (128 of them) are used as possible states. For the implementation, I'm using a second order Markov chain, meaning two previous states (two previous notes) determine the next state and nothing else. All of the transition probabilites are stored in a 2^14x2^7 matrix. As input, the composer takes two integer values (0 <= n, m <= 127) representing 2 starting notes. Based on that the algorithm calculates/generates the next note and the generation process goes *ad infinitum* (until you stop it, that is). Pitch of the notes and spacing between two notes is also stored in the Markov chain.

## Samples
Just an example what can Markov composer do. Based on a little bit of testing, there are probably a lot more, a lot better generated compositions.

* [Piano 1](http://zx.rs/mp3/Piano1.mp3)
* [Piano 2](http://zx.rs/mp3/Piano2.mp3)
* [Piano 3](http://zx.rs/mp3/Piano3.mp3)
* [Piano 4](http://zx.rs/mp3/Piano4.mp3)

## Live demonstration
If you want to see MarkovComposer in action, but you don't want to mess with the Java code, you can access a web version of it [here](http://markov.zx.rs/). Source of the web version is avaiable [here](https://github.com/anbud/MarkovComposerWeb).

## Implementation
A detailed explanation can be found in the following blog post: [Markov Composer - Using machine learning and a Markov chain to compose music](http://zx.rs/3/Markov-Composer---Using-machine-learning-and-a-Markov-chain-to-compose-music/)
