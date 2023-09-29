Prerequisites:
Always create two feature branches (frontend + backend) even if your feature only affects one of them

DEV:
1. Acceptance tests by dev [OpenShift feature environment]

-> Ready for review

2. Pull-request reviewed + approved (including Code Coverage) by reviewer [GitHub]
3. Inform dev after review through MEGA-PR (by reviewer) [Chat Space]
4. Version upgrade by dev [GitHub Issue Milestone + maven revision]
5. Merge to main by dev [GitHub]

-> Ready for deployment

6. Integration and Unit tests on main environment passed [Workflow]
7. Deployed to the test environment [GitOps Workflow triggered]

-> Ready for testing

PO:
8. Acceptance tests on the test enviroment passed
9. Inspected and approved

-> Ready for release
