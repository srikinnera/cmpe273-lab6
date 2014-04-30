package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class ConsistentHash<CacheServiceInterface> {

  private final HashFunction hashFunction;
 // private final int numberOfReplicas;
  private final SortedMap<Integer, CacheServiceInterface> circle =
    new TreeMap<Integer, CacheServiceInterface>();
  private static int i = 0;

  public ConsistentHash(HashFunction hashFunction,ArrayList<CacheServiceInterface> nodes)
  /*  int numberOfReplicas,*/  {

    this.hashFunction = Hashing.md5();
 //   this.numberOfReplicas = numberOfReplicas;

    for (CacheServiceInterface node : nodes) {
      add(node);
    }
  }

  public void add(CacheServiceInterface node) {
   // for (int i = 0; i < numberOfReplicas; i++) {
      int key = hashFunction.hashInt(i).hashCode();
      circle.put(key, node);
  //    System.out.println("i " + i + "hash" + key );
      i++;
  //  }
  }

  public void remove(CacheServiceInterface node) {
 {
      circle.remove(hashFunction.hashInt(i));     //Cannot remove any node, only last node always
      i--;}

  }

  public CacheServiceInterface get(int key) {
    if (circle.isEmpty()) {
      return null;
    }
    int temp = key % i;
    int hash = hashFunction.hashInt(temp).hashCode();
   // System.out.println("Key "+ key + "hash "+hash);
    if (!circle.containsKey(hash)) {
      SortedMap<Integer, CacheServiceInterface> tailMap =
        circle.tailMap(hash);
      hash = tailMap.isEmpty() ?
             circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(hash);
  } 

}