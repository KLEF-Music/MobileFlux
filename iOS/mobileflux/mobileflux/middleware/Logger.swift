//
//  Logger.swift
//  mobileflux
//
//  Created by RORY KELLY on 23/09/2020.
//

import Foundation
import RxSwift

protocol Logger {
    func log(loggable: String)
}

protocol Loggable {
    func log() -> String
}


func withLogger<S: State>(logger: Logger?) -> (Store<S>) -> Completable {
    return { store in
        return store.actions
            .do(onNext: { it in logger?.log(loggable: "dispatched: \(it) state: \(store.currentViewState()) ")})
                .ignoreElements()
    }
}
       
    
