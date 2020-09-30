//
//  Dispatcher.swift
//  Curated
//
//  Created by Valentin Hinov on 16/10/2018.
//  Copyright © 2018 Valentin Hinov. All rights reserved.
//

import RxSwift
import RxCocoa

public class Dispatcher {

    public static let shared = Dispatcher()

    private let resultsRelay = PublishRelay<Result>()
    public let results: Observable<Result>

    private init() {
        results = resultsRelay.asObservable()
    }

    public func dispatch(result: Result) {
        resultsRelay.accept(result)
    }
}
