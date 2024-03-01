//
//  LoginView.swift
//  iosApp
//
//  Created by Brandon McAnsh on 2/18/24.
//

import BotStacks_ChatSDK
import SwiftUI

struct LoginView: View {
    
    @EnvironmentObject var router: Router
    
    @State private var isLoadingView: Bool = false
    
    @MainActor
    private func login() async throws {
        try await BotStacksChat.companion.shared.login(
            accessToken: nil,
            userId: "1",
            username: "testuser",
            displayName: "testuser",
            picture: nil
        )
    }
    
    var body: some View {
        VStack {
            if (isLoadingView) {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
            } else {
                Button {
                    isLoadingView = true
                    Task {
                        do {
                            try await login()
                            router.navigate(to: .chats)
                        } catch {
                            isLoadingView = false
                        }
                    }
                } label: {
                    Text("Login")
                }
            }
        }.navigationBarBackButtonHidden()
    }
}
