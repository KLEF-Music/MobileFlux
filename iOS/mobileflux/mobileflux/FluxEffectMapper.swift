//
//  FluxEffectMapper.swift
//  Curated
//
//  Created by Valentin Hinov on 18/10/2018.
//  Copyright © 2018 Valentin Hinov. All rights reserved.
//

import Foundation
import RxSwift

public protocol FluxEffectMapper {
    associatedtype Effects: Effect

    func mapToEffect(result: Result) -> Effects?
}

extension FluxEffectMapper {
    public func toEffectStream(_ resultFilter: FluxResultFilter = DefaultResultFilter()) -> Observable<Effects> {
        return createEffectStream(mapper: self, resultFilter: resultFilter)
    }
}

private func createEffectStream<M: FluxEffectMapper>(mapper: M, resultFilter: FluxResultFilter) -> Observable<M.Effects> {
    return Dispatcher.shared.results
        .flatMap { result -> Observable<M.Effects> in
            if resultFilter.allowResult(result) {
                guard let effect = mapper.mapToEffect(result: result) else {
                    return Observable.empty()
                }
                return Observable.just(effect)
            } else {
                return Observable.empty()
            }
    }
}
