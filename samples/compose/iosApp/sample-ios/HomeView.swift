//
//  HomeView.swift
//  iosApp
//
//  Created by Brandon McAnsh on 2/18/24.
//

import BotStacksSDK
import SwiftUI

struct HomeView: View {
    
    @EnvironmentObject var router: Router
    
    var body: some View {
        VStack {
            Button {
                router.navigate(to: .login)
            } label: {
                Text("Login")
            }
            .padding(.top, 4)
        }.navigationBarBackButtonHidden()
    }
}
    
