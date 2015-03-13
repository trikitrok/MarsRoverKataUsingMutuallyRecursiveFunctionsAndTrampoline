(ns mars-rover.core-test
  (:use midje.sweet)
  (:require [mars-rover.core :refer (receive)]
            [mars-rover.rover :refer (rover)]
            [mars-rover.worlds :refer (square-world)]))

(facts 
  "about mars rover"
  
  (facts 
    "rotations"
    
    (facts 
      "when facing north"
      
      (fact 
        "it turns right"
        (receive 
          (rover 0 0 :north)
          "r") => (rover 0 0 :east))
      
      (fact 
        "it turns left"
        (receive 
          (rover 0 0 :north) 
          "l") => (rover 0 0 :west)))
    
    (facts
      "when facing south"
      
      (fact 
        "it turns right"
        (receive
          (rover 0 0 :south)
          "r") => (rover 0 0 :west))
      
      (fact 
        "it turns left"
        (receive
          (rover 0 0 :south)
          "l") => (rover 0 0 :east)))
    
    (facts
      "when facing east"
      
      (fact 
        "it turns right"
        (receive
          (rover 0 0 :east)
          "r") => (rover 0 0 :south))
      
      (fact 
        "it turns left"
        (receive
          (rover 0 0 :east)
          "l") => (rover 0 0 :north)))
    
    (facts 
      "when facing west"
      
      (fact 
        "it turns right"
        (receive
          (rover 0 0 :west)
          "r") => (rover 0 0 :north))
      
      (fact 
        "it turns left"
        (receive
          (rover 0 0 :west)
          "l") => (rover 0 0 :south))))
  
  (facts 
    "movements"
    
    (facts 
      "when facing north"
      
      (fact 
        "it moves forwards"
        (receive
          (rover 0 0 :north)
          "f") => (rover 0 1 :north))
      
      (fact 
        "it moves backwards"
        (receive
          (rover 0 0 :north)
          "b") => (rover 0 -1 :north)))
    
    (facts 
      "when facing south"
      
      (fact 
        "it moves forwards"
        (receive
          (rover 0 0 :south)
          "f") => (rover 0 -1 :south))
      
      (fact 
        "it moves backwards"
        (receive
          (rover 0 0 :south)
          "b") => (rover 0 1 :south)))
    
    (facts 
      "when facing east"
      
      (fact 
        "it moves forwards"
        (receive
          (rover 0 0 :east)
          "f") => (rover 1 0 :east))
      
      (fact 
        "it moves backwards"
        (receive
          (rover 0 0 :east)
          "b") => (rover -1 0 :east)))
    
    (facts 
      "when facing west"
      
      (fact 
        "it moves forwards"
        (receive
          (rover 0 0 :west)
          "f") => (rover -1 0 :west))
      
      (fact 
        "it moves backwards"
        (receive
          (rover 0 0 :west)
          "b") => (rover 1 0 :west))))
  
  (fact 
    "it can receive several messages"
    (receive
      (rover 0 0 :north)
      "brfflbrbrff") => (rover 1 -4 :south))
  
  (facts
    "being wrapped into a world"
    
    (let [world (square-world 1 1 2)]
      (receive
        (rover 1 3 :north)
        "f"
        :world world) => (rover 1 2 :north)
      
      (receive
        (rover 1 1 :south)
        "f"
        :world world) => (rover 1 2 :south)
      
      (receive
        (rover 3 1 :east)
        "f"
        :world world) => (rover 2 1 :east)
      
      (receive
        (rover 1 1 :west)
        "f"
        :world world) => (rover 2 1 :west)))
  
  (facts 
    "hitting obstacles"
    
    (let [world (square-world 1 1 2 {:x 2 :y 2} {:x 2 :y 1})]
      (receive
        (rover 1 2 :east)
        "f"
        :world world) => (rover 1 2 :east)
      
      (receive
        (rover 1 2 :east)
        "lfrf"
        :world world) => (rover 2 3 :east)
      
      (receive
        (rover 1 2 :east)
        "lfrfrf"
        :world world) => (rover 2 3 :south)
      
      (fact 
        "initial position not being on an obstacle"
        (receive
          (rover 2 2 :east) "f":world world) 
        => (throws IllegalArgumentException "Initial position is on an obstacle!")))))