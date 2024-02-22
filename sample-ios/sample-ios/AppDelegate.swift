//
//  AppDelegate.swift
//  sample-ios
//
//  Created by Brandon McAnsh on 2/20/24.
//

import SwiftUI
import Foundation
import GoogleMaps

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

      // Load the Google maps API key from the AppSecrets.plist file
      let filePath = Bundle.main.path(forResource: "AppSecrets", ofType: "plist")!
      let plist = NSDictionary(contentsOfFile: filePath)!
      let googleMapsApiKey = plist["GOOGLE_MAPS_API_KEY"] as! String

      if (!googleMapsApiKey.isEmpty) {
          GMSServices.provideAPIKey(googleMapsApiKey)
      }
    return true
  }
}
