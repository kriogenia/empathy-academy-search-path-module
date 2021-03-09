#Week 3

#08/03/2021

Today we had our meeting with the path owner. 
This week will be focused on the tokenization part.
We learn how to build the indices and what we should take into account when we do it.

First, to implement the new configurations of the indexing I decided to make a change to how I made the Indexer.
Instead of implement the interface on different classes for each index I did extract the configuration.
This way I have an Indexer class that works with an IndexConfiguration.

So, now I can't create new indices with different configurations without the need of building a new searcher.
I'm still thinking about extracting all that indexing configuration to files instead of classes.
As for now, I think I'll leave it like this.

#09/03/2021

