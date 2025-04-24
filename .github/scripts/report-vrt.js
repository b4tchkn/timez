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

  let body;
  if (noDiff) {
    body = await core.summary
        .addRaw("**✨✨ That's perfect, there is no visual difference! ✨✨**", true)
        .addRaw(`🔵 Passing: ${stats.passing}`)
        .stringify();
  } else {
    body = await core.summary
        .addRaw("**Detected visual differences**", true)
        .addTable([
          ["🔴 Changed",  "⚪️ New",       "⚫️ Deleted",  "🔵 Passing"],
          [stats.changed, stats.newItems, stats.deleted, stats.passing]
        ])
        .addHeading("📊 Download Report", 3)
        .addLink("View Report", "https://www.youtube.com/")
        .stringify()
  }

  github.rest.issues.createComment({
    issue_number: context.issue.number,
    owner: context.repo.owner,
    repo: context.repo.repo,
    body: body,
  });
}
