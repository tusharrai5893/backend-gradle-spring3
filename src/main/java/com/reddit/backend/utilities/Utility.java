package com.reddit.backend.utilities;

import com.reddit.backend.models.Comment;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Component
public class Utility {

    public static Set<Comment> removeRedundantComments(Set<Long> visitedCommentIdSet, Set<Comment> comments) {
        Iterator<Comment> iter = comments.iterator();
        while (iter.hasNext()) {
            Comment comment = iter.next();
            boolean isAddedInVisitedSet = visitedCommentIdSet.add(comment.getCommentId());
            if (isAddedInVisitedSet) {
                removeRedundantComments(visitedCommentIdSet, comment.getChildren());
            } else {
                iter.remove();
            }

        }
        return comments;
    }
}
