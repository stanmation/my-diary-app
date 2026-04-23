//
//  CreateTimelineSheet.swift
//  iosApp
//
//  Created by Stanley Darmawan on 21/4/2026.
//

import SwiftUI
import Shared

struct CreateTimelineSheet: View {
    @ObservedObject var observable: TimelineListObservable

    var body: some View {
        VStack(spacing: 20) {
            Text("New Timeline")
                .font(.title2.bold())
    

            TextField(
                "Enter name",
                text: Binding(
                    get: { observable.state!.newTimelineName },
                    set: { observable.onNameChanged(name: $0) }
                )
            )
            .textFieldStyle(.roundedBorder)

            Picker(
                "Category",
                selection: Binding(
                    get: { observable.state!.selectedCategory },
                    set: { observable.onCategorySelected(category: $0) }
                )
            ) {
                let categories = Shared.Category.companion.allValues
                ForEach(categories, id: \.self) { category in
                    Text(category.displayName())
                        .tag(category)
                }
            }

            HStack {
                Button("Cancel") {
                    observable.onCancelCreate()
                }

                Button("Create") {
                    observable.createTimeline()
                }
                .disabled(observable.state?.newTimelineName.trimmingCharacters(in: .whitespaces).isEmpty == true)
            }

            Spacer()
        }
        .padding()
    }
}
