//
//  Untitled.swift
//  iosApp
//
//  Created by Stanley Darmawan on 21/4/2026.
//

import SwiftUI

struct TimelineListView: View {
    @StateObject private var observable = TimelineListObservable()

    var body: some View {
        NavigationStack {
            VStack {
                if let state = observable.state {
                    if state.timelines.isEmpty {
                        TimelineListEmptyStateView()
                    }
                } else {
                    List {
                        ForEach(observable.state?.timelines ?? [], id: \.id) { timeline in
                            NavigationLink(destination: Text("Detail")) {
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(timeline.name)
                                        .font(.headline)

                                    Text("\(timeline.photoCount) photos")
                                        .font(.caption)
                                        .foregroundColor(.gray)
                                }
                                .padding(.vertical, 8)
                            }
                        }
                        .onDelete { indexSet in
                            indexSet.forEach { index in
                                observable.deleteTimeline(index: index)
                            }
                        }
                    }
                }
            }
            .navigationTitle("My Timelines")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        observable.onAddClicked()
                    } label: {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $observable.isShowingCreate) {
                CreateTimelineSheet(observable: observable)
            }
        }
    }
}

#Preview {
    TimelineListView()
}
