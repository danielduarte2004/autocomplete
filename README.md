# Project 4: Autocomplete

I developed two different Java implementations of a Google-like autocomplete algorithm. One used binary
search and the other one used a hash table. The program also contains a UI that shows the user the autocompleted
suggestions for every key stroke. As part of this project, I also performed benchmark analyses on
both algorithms to compare their respective efficiencies. The code is in my GitHub account.

## Outline 
- [Project Introduction](#project-introduction)
- [Running Autocomplete Main](#running-autocomplete-main)
- [Benchmarking and Analysis](#benchmarking-and-analysis)

## Project Introduction	

Autocomplete is an algorithm used in many modern software applications. In all of these applications, the user types text and the application suggests possible completions for that text as shown in the example images below taken from google search.

<details>
<summary>Expand for examples</summary>
The left/first was taken in March 2019, the right/second on October 9, 2020)
<div align="center">
  <img width="384" height="344 "src="p4-figures/googleSearch.png">
  <img width="384" height="345" src="p4-figures/googleSearch2.png">
</div>

</details>

Although finding terms that contain a query by searching through all possible results is possible, these applications need some way to select only the most useful terms to display (since users will likely not comb through thousands of terms, nor will obscure terms like "duke cookiemonster" be useful to most users). Thus, autocomplete algorithms not only need a way to find terms that start with or contain the prefix, but a way of determining how likely each one is to be useful to the user and displaying "good" terms first. This all needs to be done efficiently so that a user can see completions in real time.

In this project, you will leverage a `Comparator` in Java as well as the binary search algorithm on sorted data to implement an efficient autocompleter. You will create a second implementation based on a `HashMap`. You will then benchmark and analyze the tradeoffs of these implementations.  

<details>
<summary>Expand for more optional background on autocomplete</summary>

According to one study, in order to be useful the algorithm must do all this in less than 100 milliseconds (see article linked below). If it takes any longer, the user will already be inputting the next keystroke (while humans do not on average input one keystroke every 50 milliseconds, additional time is required for server communication, input delay, and other processes). Furthermore, the server must be able to run this computation for every keystroke, for every user. In this assignment, you will be implementing autocomplete using three different algorithms and data structures. Your autocomplete will be different than the industrial examples described above in two ways:

1. Each term will have a predetermined, constant weight/likelihood, whereas actual autocomplete algorithms might change a term's likelihood based on previous searches.
2. We will only consider terms which start with the user query, whereas actual autocomplete algorithms (such as the web browser example above) might consider terms which contain but do not start with the query.

The article linked below describes one group's recent analysis of different data structures to implement autocomplete efficiently. You'll be implementing a version of what they call a prefix hash tree, though we'll use a prefix hash list which is more efficient when terms aren't updated dynamically.
- https://medium.com/@prefixyteam/how-we-built-prefixy-a-scalable-prefix-search-service-for-powering-autocomplete-c20f98e2eff1

### Acknowledgements
The assignment was developed by Kevin Wayne and Matthew Drabick at Princeton University for their Computer Science 226 class. Former head CompSci 201 UTAs, Arun Ganesh (Trinity '17) and Austin Lu (Trinity '15) adapted the assignment for Duke with help from Jeff Forbes. Josh Hug updated the assignment and provided more of the testing framework.

</details>

## Running Autocomplete Main

When you fork and clone the project you'll be able to run the `main` method of `AutocompleteMain`. Doing so will launch a "GUI" (Graphical User Interface) that allows you to select a data file. The data file will determine the set of possible words to be recommended by the autocompleter application, and also includes weights for how common the words are. Several such files are included along with this project.

Once you select a file, the GUI will prompt you to enter a term. As you type, you should see the most common words that complete what you have typed so far appearing. For example, if you run `AutocompleteMain` and select the file `words-333333.txt` from the data folder you should see the output below shown in the GUI window for the search query **auto**.  You'll use this same search term, `auto` to test the other implementations you develop.

<details>
<summary>Expand for example of program running</summary>
<div align="center">
  <img src="p4-figures/astrachanSearch.png">
</div>
</details>

By default, `AutocompleteMain` is using `BruteAutocomplete` to find the correct words to display. You will write two additional implementations of the `Autocompleter` interface: `BinarySearchAutocomplete` and `HashListAutocomplete`. When you finish one, you can again run `AutocompleteMain` using your new implementation by changing the `AUTOCOMPLETOR_CLASS_NAME` just before the `main` method of `AutocompleteMain`.

## Benchmarking and Analysis

You'll submit the analysis as a PDF separate from the code in Gradescope. 

**Question 1.** Inside of `BenchmarkForAutocomplete`, uncomment the two other implementation names so that `myCompletorNames` has all three Strings: `"BruteAutocomplete"`, `"BinarySearchAutocomplete"`, and `"HashListAutocomplete"` (if you want to benchmark only a subset of these, perhaps because one isn't working, just leave it commented).

Run `BenchmarkForAutocomplete` three times, once for each of the files in the Benchmark program: `threeletterwords.txt`, `fourletterwords.txt`, and `alexa.txt`. You can change which file is being used inside of the `doMark` method. **Copy and paste all three results into your analysis**. An example and detailed information about the output is described in the expandable section below.

<details>
<summary>Expand for details on Benchmark results</summary>

On Professor Fain's laptop, the first few lines are what's shown below for `data/threeletterwords.txt` (in addition, the `sizeInBytes` for the implementations are shown at the bottom). These numbers are for a file of every three letter word "aaa, "aab", … "zzy", "zzz", not actual words, but 3-character strings. All times are listed in seconds.

- The `init time` data shows how long it took to initialize the different implementations.
- The `search` column shows the prefix being used to search for autocompletions; unlabeled "search" is for an empty string `""` which matches on every term. 
- The `size` column shows how many terms have `search` as a prefix. This is described as `M` earlier in [part 4](#part-4-finish-implementing-topmatches-in-binarysearchautocomplete).
- `#match` shows the number of highest weight results being returned by `topMatches`. This is described as `k` earlier in [part 4](#part-4-finish-implementing-topmatches-in-binarysearchautocomplete).
- The next three columns give the running time in seconds for `topMatches` with the given parameters for the different implementations.


```
init time: 0.004612     for BruteAutocomplete
init time: 0.003348     for BinarySearchAutocomplete
init time: 0.03887      for HashListAutocomplete
search  size    #match  BruteAutoc      BinarySear      HashListAu
        17576   50      0.00191738      0.00306458      0.00001950
        17576   50      0.00039575      0.00198267      0.00000546
a       676     50      0.00034438      0.00014479      0.00000942
a       676     50      0.00035567      0.00015113      0.00000350
b       676     50      0.00016033      0.00011954      0.00000292
...
```

</details>

**Question 2.** Let `N` be the total number of terms, let `M` be the number of terms that prefix-match a given `search` term (the `size` column above), and let `k` be the number of highest weight terms returned by `topMatches` (the `#match` column above). The runtime complexity of `BruteAutocomplete` is `O(N log(k))`. The runtime complexity of `BinarySearchAutocomplete` is `O(log(N) + M log(k))`. Yet you should notice (as seen in the example timing above) that `BruteAutocomplete` is similarly efficient or even slightly more efficient than `BinarySearchAutocomplete` on the empty `search` String `""`. Answer the following:
- For the empty `search` String `""`, does `BruteAutocomplete` seem to be asymptotically more efficient than `BinarySearchAutocomplete` with respect to `N`, or is it just a constant factor more efficient? To answer, consider the different data sets you benchmarked with varying `size`.
- Explain why this observation (that `BruteAutocomplete` is similarly efficient or even slightly more efficient than `BinarySearchAutocomplete` on the empty `search` String `""`) makes sense given the values of `N` and `M`. 
- With respect to `N` and `M`, when would you expect `BinarySearchAutocomplete` to become more efficient than `BruteAutocomplete`? Does the data validate your expectation? Refer specifically to your data in answering.


**Question 3.** Run the `BenchmarkForAutocomplete` again using `alexa.txt` but doubling `matchSize` to `100` (`matchSize` is specified in the `runAM` method). Again copy and paste your results. Recall that `matchSize` determines `k`, the number of highest weight terms returned by `topMatches` (the `#match` column above). Do your data support the hypothesis that the dependence of the runtime on `k` is logarithmic for `BruteAutocomplete` and `BinarySearchAutocomplete`?

**Question 4.** Briefly explain why `HashListAutocomplete` is much more efficient in terms of the empirical runtime of `topMatches`, but uses more memory than the other `Autocomplete` implementations.
