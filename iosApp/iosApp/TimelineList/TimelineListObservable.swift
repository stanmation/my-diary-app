//
//  TimelineListObservable.swift
//  iosApp
//
//  Created by Stanley Darmawan on 21/4/2026.
//

import Foundation
import Shared
import KMPNativeCoroutinesAsync

class TimelineListObservable: ObservableObject {

    private let viewModel = TimelineListViewModel()

    @Published var state: TimelineListUiState?
    @Published var isShowingCreate: Bool = false


    init() {
        observeState()
    }

    private func observeState() {
        viewModel.observeState { state in
            self.state = state
            self.isShowingCreate = state.isShowingCreate
        }
    }

    // MARK: - Actions

    func onAddClicked() {
        viewModel.onAddClicked()
    }

    func onCancelCreate() {
        viewModel.onCancelCreate()
    }

    func onNameChanged(name: String) {
        viewModel.onNameChanged(name: name)
    }

    func onCategorySelected(category: Shared.Category) {
        viewModel.onCategorySelected(category: category)
    }

    func createTimeline() {
        viewModel.createTimeline()
    }

    func deleteTimeline(index: Int) {
        viewModel.deleteTimeline(index: Int32(index))
    }
}
