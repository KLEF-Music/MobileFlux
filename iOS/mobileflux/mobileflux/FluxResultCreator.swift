//
//  FluxResultCreator.swift
//  Curated
//
//  Created by Valentin Hinov on 18/10/2018.
//  Copyright © 2018 Valentin Hinov. All rights reserved.
//

import Foundation
import RxSwift

public protocol FluxResultCreator {
    associatedtype Events: UiEvent

    func createResults(events: Observable<Events>) -> Observable<Result>
}
