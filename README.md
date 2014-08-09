TemporalReasoning
=================

Temporal Reasoning of facts from OIE systems


Eclipse Integration
===================

1. add the git URI into Eclipse workspace from the "git" perspective
2. add "An existing project" and clone the project into your workspace.
3. It will be imported as a Maven project.
4. Do an build if "Automatically Build" option is not set


Starting Point
==============

1. ReasoningClient under client is the starting point
2. Supply the oie file of your choice (reverb or oie), followed by top-k candidates you want to fetch and the file delimiter
3. A sample input (sample.input) can be found under resources.
4. Use the shell script (processReverb.sh) under resources for parsing and cleansing the actual reverb file.
