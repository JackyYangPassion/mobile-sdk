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
    
    @ObservedObject var router = Router()
    
    init() {
        BotStacksChat.companion.shared.setup(apiKey: "l35ndgs2p75ikbzj6jh8uofk", delayLoad: false)
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

final class Router: ObservableObject {
    
    public enum Destination: Codable, Hashable {
        case chats
    }
    
    @Published var navPath = NavigationPath()
    
    func navigate(to destination: Destination) {
        navPath.append(destination)
    }
    
    func navigateBack() {
        navPath.removeLast()
    }
    
    func navigateToRoot() {
        navPath.removeLast(navPath.count)
    }
}
