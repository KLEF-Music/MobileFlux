//
//  Store.swift
//  mobileflux
//
//  Created by RORY KELLY on 23/09/2020.
//

import Foundation
import RxSwift
import RxCocoa

protocol ReduxStore {
    associatedtype S : State

    func dispatch(action: Action)

    var actions: Observable<Action> { get }

    func currentViewState() -> S

    var updates: Observable<S>{ get }

}


extension Observable where Observable.Element == Action {
   
    func reduxStore<VS: State>(
        initialStateSupplier: Single<VS>,
        reducer: @escaping Reducer<VS>
    )-> Observable<VS> {
        return initialStateSupplier
            .asObservable()
            .flatMap { initialState -> Observable<VS> in
                return self.scan(initialState) { oldState, result in
                    reducer(oldState, result)
                }
            }
            .distinctUntilChanged()
            .share(replay: 1, scope: .whileConnected)
    }
}

typealias Middleware<S: State> = (_ actions: Observable<Action>, _ dispatch: @escaping (Action) -> Void, _ state: () -> S ) -> Observable<S>


open class Store<S: State>: ReduxStore {
    typealias S = S
    
    private let resultsRelay = PublishRelay<Action>()
    var actions: Observable<Action>
    var updates: Observable<S>
 
    init(
        initialStateSupplier: Single<S>,
        reducer: @escaping Reducer<S>,
        middleware: Middleware<S>
    ) {
        actions = resultsRelay.asObservable()
        updates = actions.reduxStore(initialStateSupplier: initialStateSupplier, reducer: reducer)
        updates = Observable.merge(updates, middleware(actions, dispatch, currentViewState).asObservable())
    }
    
    func dispatch(action: Action) {
        resultsRelay.accept(action)
    }
    
    func currentViewState() -> S {
        return updates.first() as! S
    }
    
}

