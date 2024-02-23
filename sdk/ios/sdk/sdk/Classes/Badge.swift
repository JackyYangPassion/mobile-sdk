//
//  Badge.swift
//  BotStacksSDK_Example
//
//  Created by Brandon McAnsh on 2/23/24.
//  Copyright © 2024 CocoaPods. All rights reserved.
//

import Foundation
import SwiftUI
import botstacks_core

struct Badge : UIViewControllerRepresentable {
    
    @State var count: Int32
    
    func makeUIViewController(context: Context) -> UIViewController {
        BadgeKt._Badge(count: count)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

