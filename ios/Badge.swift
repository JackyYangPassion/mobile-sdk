//
//  Badge.swift
//  BotStacksSDK
//
//  Created by Brandon McAnsh on 2/23/24.
//

import Foundation
import SwiftUI
import botstacks_core

public struct Badge : UIViewControllerRepresentable {

    @State public var count: Int32

    @MainActor public init(count: Int32) {
        _count = State(initialValue: count)
    }

    public func makeUIViewController(context: Context) -> UIViewController {
        BadgeKt.Badge(count: count)
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        
    }
}

