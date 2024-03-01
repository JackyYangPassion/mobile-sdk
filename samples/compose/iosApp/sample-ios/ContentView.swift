//
//  ContentView.swift
//  iosApp
//
//  Created by Brandon McAnsh on 2/13/24.
//

import 
import SwiftUI

struct ContentView: View {
    
    @EnvironmentObject var router: Router
    
    var body: some View {
       Badge(3)
            .ignoresSafeArea()
            .navigationBarBackButtonHidden()
    }
}

#Preview {
    ContentView()
}
