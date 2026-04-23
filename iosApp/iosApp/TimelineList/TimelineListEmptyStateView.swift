//
//  EmptyStateView.swift
//  iosApp
//
//  Created by Stanley Darmawan on 21/4/2026.
//

import SwiftUI

struct TimelineListEmptyStateView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "photo.on.rectangle.angled")
                .font(.system(size: 48))
                .foregroundColor(.gray)

            Text("No Timelines Yet")
                .font(.headline)

            Text("Create your first timeline to start organizing your photos")
                .font(.subheadline)
                .foregroundColor(.gray)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
