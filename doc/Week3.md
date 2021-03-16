#Week 3

##2021-03-08

Today we had our meeting with the path owner. 
This week will be focused on the tokenization part.
We learn how to build the indices and what we should take into account when we do it.

First, to implement the new configurations of the indexing I decided to make a change to how I made the Indexer.
Instead of implement the interface on different classes for each index I did extract the configuration.
This way I have an Indexer class that works with an IndexConfiguration.

So, now I can't create new indices with different configurations without the need of building a new searcher.
I'm still thinking about extracting all that indexing configuration to files instead of classes.
As for now, I think I'll leave it like this.

##2021-03-09

First I built on top of my changes yesterday and implement a function to check if an index already exists.
Then I implemented a way to build and index using a configuration file, only called when the index doesn't exist.

The next thing I did was start to try filters with the analyzer, 
to speed up things I established some aliases on my .zshrc.
I dedicated a lot of time to the ngrams but consulting my owner I changed back to whole words.
Tomorrow I should ask about adding some config files to the ElasticSearch image.

At the end of the day I had a nice analyzer for indexing titles that I think it worked close to the popular engines I tried.
I was still lacking some things tho, like the roman or spelled numbers to swap with the arabic.

##2021-03-10

It was difficult, but I implemented a basic way of handling the number spelling.
Then I tried my analyzers with searches against the whole database, it didn't go well.
My approach with the types was bad, keeping only the wanted keywords made that everything matched the query,
So I will erase the keep filter, but I will still look for the better way to search on keyword properties.

I made a ton of tests, and that involved creating test index and classes.
It took me the whole morning, but it should save my a lot of future time.

##2021-03-11

So, a full testing day. 

First I finished the ElasticSearchEngine tests, which were the key ones.
To make it easier and more readable I extracted some common operations to a helper class.

Then I jumped to the ImdbItem ,and I found a mistake on the class while doing those.
That's good, they work and servers a purpose.

SearchResponse looked easy at first, even with the generic typing. It was not.
I had a hard time being able to test properly the json conversion. I did it tho.
SearchResult was almost the same, but it did need mocking and therefore the installation of Mockito.

##2021-03-12

First thing in the day, apart from the stand-up-like message was create a search engine function to delete an index.
It was really easy, and I should have made it previously. I'm glad I was able to do it before the meetings.

Next to the meeting I did try some things with the multi search, being able to improve it.
Then I jumped to change ImdbItem and its indexing, making the dates ase such f.e.
That led me to add annotations to all the item properties improving the contract of the item.