//
//  sample_iosApp.swift
//  iosApp
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
        // Load the API keys from secrets
        let filePath = Bundle.main.path(forResource: "AppSecrets", ofType: "plist")!
        let plist = NSDictionary(contentsOfFile: filePath)!
        let botstacksApiKey = plist["BOTSTACKS_API_KEY"] as! String
        let googleMapsApiKey = plist["GOOGLE_MAPS_API_KEY"] as! String
        let giphyApiKey = plist["GIPHY_API_KEY"] as! String
        
        BotStacksChat.companion.shared.setup(apiKey: botstacksApiKey, giphyApiKey: giphyApiKey, googleMapsApiKey: googleMapsApiKey)
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
