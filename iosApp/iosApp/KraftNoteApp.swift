import SwiftUI

@main
struct KraftNoteApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        ComposeViewControllerWrapper()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
