import UIKit
import Shared

class ComposeViewControllerWrapper: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        let composeVC = MainViewControllerKt.CreateViewController()
        addChild(composeVC)
        view.addSubview(composeVC.view)
        composeVC.view.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            composeVC.view.topAnchor.constraint(equalTo: view.topAnchor),
            composeVC.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            composeVC.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            composeVC.view.trailingAnchor.constraint(equalTo: view.trailingAnchor)
        ])
        composeVC.didMove(toParent: self)
    }
}
