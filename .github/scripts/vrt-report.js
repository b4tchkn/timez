module.exports = async ({ github, context, core }) => {
  const passing = 10;
  const endLineMessage = "This is result of vrt.";
  const markdown = await core.summary
        .addRaw("**Detected visual differences**")
        .addTable([
          ["🔴 Changed",  "⚪️ New",       "⚫️ Deleted",  "🔵 Passing"],
          ["10", "10", "0", "10"]
        ])
        .addLink("View Report", "https://www.youtube.com/")
        .addBreak()
        .addRaw(endLineMessage)
        .stringify();

  github.rest.issues.createComment({
    issue_number: context.issue.number,
    owner: context.repo.owner,
    repo: context.repo.repo,
    body: markdown
  });
};
