import Foundation
import RxSwift

public typealias Reducer<S: State> = (_ state: S, _ action: Action) -> S

func reducer<S: State>(_ reducer: @escaping Reducer<S> ) -> Reducer<S> {
    return reducer
}

func combineReducers<S: State>(_ reducers: Reducer<S>...)-> Reducer<S> {
    return reducer { state, action in
        reducers.reduce(state) { acc, r in r(acc, action) }
    }
}



