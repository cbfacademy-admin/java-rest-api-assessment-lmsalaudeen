package com.cbfacademy.apiassessment.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.cbfacademy.apiassessment.blog.Blog;

public class Algorithm {
    
    // for a list of blogs, if blog.getContent contains?, return those blogs
    public static List<Blog> searchBlogContent(List<Blog> blogs, String targetWord) {

        // a container to hold blogs of interest
        List<Blog> targetBlogs = new ArrayList<>();

        // if target word in content, add to targetBlogs
        for (Blog blog: blogs) {
            if (blog.getContent().toLowerCase().contains(targetWord.toLowerCase())) {
                // add blog to list
                targetBlogs.add(blog);
            }
        } return targetBlogs;
    }

    public static List<Blog> searchBlogAuthor(List<Blog> blogs, String authorName) {

        // a container to hold blogs of interest
        List<Blog> targetBlogs = new ArrayList<>();

        // if target author in content, add to targetBlogs
        for (Blog blog: blogs) {
            if (blog.getAuthor().toLowerCase().contains(authorName.toLowerCase())) {
                // add blog to list
                targetBlogs.add(blog);
            }
        } return targetBlogs;
    }
    // sort blog content according to .getDate
    public static void mergeSortBlog(List<Blog> blogs){
        if (blogs.size() < 2) return;

        // midIndex is the halfway point
        int midIndex = blogs.size()/2;

        // intializing the sublist with the N of items e.g a list with 7 items
        List<Blog> listOne = new ArrayList<Blog>(midIndex); // 3
        List<Blog> listTwo = new ArrayList<Blog>(blogs.size() - midIndex); //4

        for (int i = 0; i<midIndex; i++) { // i < 3
            listOne.add(blogs.get(i)); // 1. arrayOne[0] = array[0]; 2. arrayOne[1] = array[1]
        }

        for (int i = midIndex; i<blogs.size(); i++) { //i=3; i<7
            listTwo.add(blogs.get(i)); // 1. arrayTwo[3] = array[3]; 2. arrayTwo[4] = array[4];
        }

        // calling mergeSort on the 1st sublist - further division
        mergeSortBlog(listOne);

        // calling mergeSort on the 2nd sub list
        mergeSortBlog(listTwo);

        // calling merge which sorts and then merges the sub lists
        mergeBlog(blogs, listOne, listTwo);
        
    }
    
    public static void mergeBlog(List<Blog> list, List<Blog> listOne, List<Blog> listTwo) {

        int listOneCounter = 0;
        int listTwoCounter = 0;
        int listCounter = 0;

        // while there are still elements in both list -- if the counter is less than the size,
        // means all the elements have not been iterated
        while (listOneCounter < listOne.size() && listTwoCounter < listTwo.size()) {

            // compare the dates (long) of each element in list one and two
            if (listOne.get(listOneCounter).getDate().getEpochSecond() <= listTwo.get(listTwoCounter).getDate().getEpochSecond()) { 
                list.set(listCounter, listOne.get(listOneCounter)); // if it's less than, then it'll be appened to the list (ascending order)
                // mergedList.add(listOne.get(listOneCounter));
                listOneCounter++; // move to the next item in listOne
            } else { //else the item in listTwo should be appended to array (ascending order)
                list.set(listCounter, listTwo.get(listTwoCounter));
                // mergedList.add(listTwo.get(listTwoCounter));
                listTwoCounter++; // move to the next item in listTwo
            }
            listCounter++; // move to the next item in array
        } 

        // Once one of the sublists run out of elements, put the remaining items in list
        while (listOneCounter < listOne.size()) {
            list.set(listCounter, listOne.get(listOneCounter));
            listOneCounter++;
            listCounter++;
        }

        while (listTwoCounter < listTwo.size()) {
            list.set(listCounter, listTwo.get(listTwoCounter));
            listTwoCounter++;
            listCounter++;
           
        }
    };
}
