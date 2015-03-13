(ns mars-rover.rover
  (:require [mars-rover.worlds :refer [rover-not-hitting-obstacle wrap]]))

(defn rover [x y direction]
  {:x x :y y :direction direction})

(defn process [commands {direction :direction :as initial-rover} world]
  (let [wrap-rover (partial wrap world)]
    
    (letfn 
      [(facing-north 
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
            (case command
              :rotate-right 
              (facing-east (rover x y :east) rest-commands)
              
              :rotate-left 
              (facing-west (rover x y :west) rest-commands)
              
              :move-forwards
              (facing-north 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover x (inc y) :north))
                  world) 
                rest-commands)
              
              :move-backwards
              (facing-north 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover x (dec y) :north))
                  world)
                rest-commands))
            current-rover))
       
       (facing-south 
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
            (case command
              :rotate-right 
              (facing-west (rover x y :west) rest-commands)
              
              :rotate-left 
              (facing-east (rover x y :east) rest-commands)
              
              :move-forwards
              (facing-south 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover x (dec y) :south))
                  world)
                rest-commands)
              
              :move-backwards
              (facing-south 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover x (inc y) :south))
                  world)
                rest-commands))
            current-rover))
       
       (facing-east
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
            (case command
              :rotate-right 
              (facing-south (rover x y :south) rest-commands)
              
              :rotate-left 
              (facing-north (rover x y :north) rest-commands)
              
              :move-forwards
              (facing-east 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover (inc x) y :east))
                  world)
                rest-commands)
              
              :move-backwards
              (facing-east 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover (dec x) y :east))
                  world)
                rest-commands))
            current-rover))
       
       (facing-west
         [{:keys [x y] :as current-rover} [command & rest-commands]]
         #(if command
            (case command
              :rotate-right 
              (facing-north (rover x y :north) rest-commands)
              
              :rotate-left 
              (facing-south (rover x y :south) rest-commands)
              
              :move-forwards
              (facing-west 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover (dec x) y :west))
                  world)
                rest-commands)
              
              :move-backwards
              (facing-west 
                (rover-not-hitting-obstacle
                  current-rover
                  (wrap-rover (rover (inc x) y :west))
                  world)
                rest-commands))
            current-rover))
       
       (initial-state-fn 
         [direction] 
         (case direction
           :north facing-north 
           :south facing-south 
           :east facing-east 
           :west facing-west))]
      
      (trampoline (initial-state-fn direction) 
                  initial-rover 
                  commands))))