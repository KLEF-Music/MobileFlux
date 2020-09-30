//
//  Epic.swift
//  mobileflux
//
//  Created by RORY KELLY on 23/09/2020.
//

import Foundation
import RxSwift

typealias Epic<S> = (_ actions: Observable<Action>, _ state: () -> S) -> Observable<Action>

func epic<S>(epic: @escaping Epic<S>)-> Epic<S> {
    return epic
}
func emptyEpic<S> () -> Epic<S> {
    return epic { _,_ in Observable.empty() }
}

func combineEpics<S>(epics: [Epic<S>])-> Epic<S> {
    return epic { actions, state in
        Observable.from(epics.map { $0(actions, state) }).merge()
        
    }
}
func combineEpics<S>(epics: Epic<S>...)-> Epic<S> {
    return combineEpics(epics: epics)
}


func epicMiddleware<S>(epics: Epic<S>...) -> Middleware<S> {
    return { actions, dispatch, state in
        let allEpics = combineEpics(epics: epics)
        return allEpics(actions,  state )
            .do(onNext: { dispatch($0) })
            .ignoreElements()
            .asObservable()
            .map { $0 as! S }
    }
     
}

