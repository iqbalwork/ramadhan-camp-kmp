import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        AlarmeePlatformConfiguration.initialize()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}