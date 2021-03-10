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

First I built on top of my changes yesterday and implement a function to check if an index already exists.
Then I implemented a way to build and index using a configuration file, only called when the index doesn't exist.

The next thing I did was start to try filters with the analyzer, 
to speed up things I established some aliases on my .zshrc.
I dedicated a lot of time to the ngrams but consulting my owner I changed back to whole words.
Tomorrow I should ask about adding some config files to the ElasticSearch image.

At the end of the day I had a nice analyzer for indexing titles that I think it worked close to the popular engines I tried.
I was still lacking some things tho, like the roman or spelled numbers to swap with the arabic.

#10/03/2021

It was difficult, but I implemented a basic way of handling the number spelling.
Then I tried my analyzers with searches against the whole database, it didn't go well.
My approach with the types was bad, keeping only the wanted keywords made that everything matched the query,
So I will erase the keep filter, but I will still look for the better way to search on keyword properties.

I made a ton of tests, and that involved creating test index and classes.
It took me the whole morning, but it should save my a lot of future time.


