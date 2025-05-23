module.exports = async ({ github, context, core }) => {
  const fs = require('fs');

  const log = fs.readFileSync('.reg/report.json', 'utf-8');
  const json = JSON.parse(log);
  console.log(json);

  const stats = {
    changed: json.failedItems.length.toString(),
    newItems: json.newItems.length.toString(),
    deleted: json.deletedItems.length.toString(),
    passing: json.passedItems.length.toString()
  };

  const noDiff = (stats.changed === "0" && stats.newItems === "0" && stats.deleted === "0");
  const endLineMessage = "This is result of vrt."
  const headRef = context.payload.pull_request.head.ref;
  const url = `https://b4tchkn.github.io/timez/${headRef}`;

  let body;
  if (noDiff) {
    body = await core.summary
        .addRaw("**✨✨ That's perfect, there is no visual difference! ✨✨**\n")
        .addRaw(`🔵 Passing: ${stats.passing}\n`)
        .addLink("View Report", url)
        .addBreak()
        .addRaw(endLineMessage)
        .stringify();
  } else {
    body = await core.summary
        .addRaw("**Detected visual differences**")
        .addTable([
          ["🔴 Changed",  "⚪️ New",       "⚫️ Deleted",  "🔵 Passing"],
          [stats.changed, stats.newItems, stats.deleted, stats.passing]
        ])
        .addLink("View Report", url)
        .addBreak()
        .addRaw(endLineMessage)
        .stringify();
  }



  const existingCommentId = await findExistingCommentId(github, context, endLineMessage);

  if (existingCommentId) {
    github.rest.issues.updateComment({
        comment_id: existingCommentId,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: body,
     });
  } else {
    github.rest.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: body,
    });
  }
}

async function findExistingCommentId(github, context, identifier) {
  const owner = context.repo.owner;
  const repo = context.repo.repo;
  const issue_number = context.issue.number;
  let commentIdToUpdate = null;

  try {
    const { data: comments } = await github.rest.issues.listComments({
      owner,
      repo,
      issue_number,
    });

    for (const comment of comments) {
      if (comment.user.type === "Bot" && comment.body.includes(identifier)) {
        commentIdToUpdate = comment.id;
        break;
      }
    }
  } catch (error) {
    return null;
  }

  return commentIdToUpdate;
}
