# Isthmas

(Could be the acronym of Intelligent, Sexy and Totally Heroic Multi-Agent System, but actually is from isthmus.)

A Clojure application allowing to run agents in a big society of agents managing relations between articles, jobs, authors and well, ... anything you want really.

## Usage

### Programming agents

An agent is an instance of `Agent` and it can receive messages. (See `mas.agent/receive`)

### Running the application

You are going to need `Leiningen 2.x`. Once Leiningen is installed you can run

    lein run

which should launch the application. Just go to [http://localhost:8888/] now to check if everything is okay.
