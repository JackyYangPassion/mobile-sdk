//
//  sample_iosApp.swift
//  sample-ios
//
//  Created by Brandon McAnsh on 2/13/24.
//

import SwiftUI
import BotStacksSDK

@main
struct sample_iosApp: SwiftUI.App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @ObservedObject var router = Router()
    
    init() {
        // Load the Google maps API key from the AppSecrets.plist file
        let filePath = Bundle.main.path(forResource: "AppSecrets", ofType: "plist")!
        let plist = NSDictionary(contentsOfFile: filePath)!
        let botstacksApiKey = plist["BOTSTACKS_API_KEY"] as! String
        
        BotStacksChat.companion.shared.setup(apiKey: botstacksApiKey, delayLoad: false)
    }
    

    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $router.navPath) {
                LoginView()
                .navigationDestination(for: Router.Destination.self) { destination in
                    switch destination {
                    case .chats:
                        ContentView()
                    }
                }
            }
            .environmentObject(router)
        }
    }
}
