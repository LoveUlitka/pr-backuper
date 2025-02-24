# Pull request backup
Pull request backup is a tool that allows to export data about all open pull requests (pr info and all actions).
It is written using Scala (the best possible language in this world and all the others). 

## Authentication
Pull request backup uses Bitbucket user API and requires `BITBUCKETSESSIONID` in order to process the requests.
You should find your session id and add it to `secret.conf` (`token` key).

### How to find your session id?
1. Open a new tab in the browser and open browser console aka dev tools 
2. Open https://your-bitbucket-server/projects
3. Find any request to https://your-bitbucket-server/rest (for example `bundled`)
4. In request info find `BITBUCKETSESSIONID` cookie
5. Paste this string to `secret.conf` file (`token` key)
6. Be careful! Don't share this cookie with anyone else! Don't post it anywhere!

## How to run locally
1. Find your session id and add it to `secret.conf` (`token` key)
2. Set your Bitbucket Server url in `secret.conf` (`url` key)
3. Set projects list in `application.conf` (`projects` key)
4. Run via sbt `runMain pull.request.backup.Application` 
5. or run in the ide

## Output
Exported data (pull requests info and actions) is saved to `prs-info` folder:
```
pull-request-backup
--> src
--> prs-info
    --> project-name-0
    --> project-name-1
    --> project-name-2
        --> repo-name-0
            --> JIRAKEY-1000-PR-2000-1736950839037-activities.json
            --> JIRAKEY-1000-PR-2000-1736950839037-info.json
```
`info` file contains the title, description, author, reviewers, etc.
`activities` file contains the pull request actions timeline (comments, approvals, etc).
