//
//  ContentView.swift
//  sample-ios
//
//  Created by Brandon McAnsh on 2/13/24.
//

import BotStacksSDK
import SwiftUI

struct BotStacksChatController : UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        BotStacksChatControllerKt.BotStacksChatController {
                
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
    
}

struct ContentView: View {
    
    @EnvironmentObject var router: Router
    
    var body: some View {
        BotStacksChatController()
            .ignoresSafeArea()
            .navigationBarBackButtonHidden()
    }
}

#Preview {
    ContentView()
}
