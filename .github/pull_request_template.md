**Prerequisites: Always create two feature branches (frontend + backend) even if your feature only affects one of them**

**DEV:**
> Development
- [ ] Acceptance tests by dev [OpenShift feature environment]
  - [ ] Moved to "Ready for review"

> Review
- [ ] Pull-request reviewed + approved (including Code Coverage) by reviewer [GitHub]
- [ ] Inform dev after review through MEGA-PR (by reviewer) [Chat Space]
- [ ] Version upgrade by dev [GitHub Issue Milestone + maven revision]
- [ ] Merge to main by dev [GitHub]
  - [ ] Moved to "Ready for deployment"

> Deployment
- [ ] Integration and Unit tests on main environment passed [Workflow]
- [ ] Deployed to the test environment [GitOps Workflow triggered]
  - [ ] Moved to "Ready for testing"

**PO:**
> Testing
- [ ] Acceptance tests on the test enviroment passed
- [ ] Inspected and approved
  - [ ] Moved to "Ready for release"
