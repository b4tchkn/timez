module.exports = async ({ github, context, core }) => {
  const passing = 10
  const markdown = await core.summary
    .addRaw("**✨✨ That's perfect, there is no visual difference! ✨✨**", true)
    .addRaw(`🔵 Passing: ${passing}`)
    .addLink("View Report", "https://www.youtube.com/")
    .stringify();

  github.rest.issues.createComment({
    issue_number: context.issue.number,
    owner: context.repo.owner,
    repo: context.repo.repo,
    body: markdown
  });
};
