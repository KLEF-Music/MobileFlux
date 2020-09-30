//
//  FluxTypes.swift
//  Curated
//
//  Created by Valentin Hinov on 16/10/2018.
//  Copyright © 2018 Valentin Hinov. All rights reserved.
//

import Foundation

public protocol CustomEquatable {
    func isEqualTo(_ other: CustomEquatable) -> Bool
}

/**
 Due to type limitations, we cannot make `Result` conform to `Equatable` directly.
 Instead we make it conform to `CustomEquatable`. It's highly advised that all implementations
 of `Result` themselves conform to `Equatable`.
*/
public protocol Result: CustomEquatable {}
public protocol UiEvent: Equatable {}
public protocol Effect: Equatable {}

public struct EmptyEffect: Effect {}
public struct EmptyResult: Result {}

extension CustomEquatable {
    public func isEqualTo(_ other: CustomEquatable) -> Bool {
        return false
    }
}

extension CustomEquatable where Self: Equatable {
    public func isEqualTo(_ other: CustomEquatable) -> Bool {
        return (other as? Self) == self
    }
}

extension ViewState {
    public func copy(_ updateOp: (inout Self) -> Void) -> Self {
        var newState = self
        updateOp(&newState)
        return newState
    }
}
