//
//  ContentView.swift
//  sample-ios
//
//  Created by Brandon McAnsh on 2/13/24.
//

import BotStacksSDK
import SwiftUI

struct BotStacksNavigationView : UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        BotStacksNavigationKt.BotStacksNavigation()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
    
}

struct ContentView: View {
    var body: some View {
        BotStacksNavigationView().ignoresSafeArea()
    }
}

#Preview {
    ContentView()
}
