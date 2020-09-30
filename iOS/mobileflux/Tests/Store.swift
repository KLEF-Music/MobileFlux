//
//  Store.swift
//  Tests
//
//  Created by RORY KELLY on 23/09/2020.
//

import Foundation

import Quick
import Nimble
import RxSwift
@testable import mobileflux

struct Scorekeeper : State{
    let runningScore: Int
    let climbingScore: Int
    
    
    init(runningScore: Int = 0, climbingScore: Int = 0) {
        self.runningScore = runningScore
        self.climbingScore = climbingScore
    }
    
    init(scoreKeeper: Scorekeeper, runningScore: Int? = nil, climbingScore: Int? = nil) {
        self.runningScore = runningScore ?? scoreKeeper.runningScore
        self.climbingScore = climbingScore ?? scoreKeeper.climbingScore
    }
    
    func incrementRunningScoreBy(points: Int) -> Scorekeeper {
          return Scorekeeper(scoreKeeper: self, runningScore: self.runningScore + points)
      }
}


class StoreTests: QuickSpec {
 

    class TestAction: Action
    
    override func spec() {
      describe("Store") {
        
        let storer = reducer{ (oldState: Scorekeeper, action) in
            oldState.incrementRunningScoreBy(points: 1)
        }
        let store = Store(
            initialStateSupplier: Single.just(Scorekeeper()),
            reducer: storer,
            Middleware: emp
            )

        
        it("updates state") {
            store.dispatch(TestAction)
            
        }

        context("if it doesn't have what you're looking for") {
          it("needs to be updated") {
           
          }
        }
      }
    }
}
