ThisBuild / scalaVersion := "2.13.15"

// scala compilation options
val scalaFlags: Seq[String] = Seq(
  "-deprecation",
  "-feature",
  "-language:postfixOps",
  "-Xlint:-missing-interpolator,_", // Better warnings
  "-unchecked",                     // Better warning messages on unchecked conversions
  "-opt:l:method",                  // Enable optimizations
  "-opt:l:inline",                  // Enable inlining
  "-opt-inline-from:scala.**",      // Consider inlining anywhere it is possible
  "-opt-warnings:at-inline-failed", // Warns when inlining fails
  "-Ymacro-annotations" // Needed for Monocle macros
)

// Let's be paranoid
val paranoidFlags: Seq[String] = Seq(
  "-explaintypes",                 // Explain type errors in more detail.
  "-language:existentials",        // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros", // Allow macro definition (besides implementation and application)
  "-language:higherKinds",         // Allow higher-kinded types
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-Xcheckinit",                   // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings",              // Fail the compilation if there are any warnings.
  "-Xlint:adapted-args",           // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant",               // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select",     // Selecting member of DelayedInit.
  "-Xlint:doc-detached",           // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible",           // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any",              // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator",   // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-unit",           // Warn when nullary methods return Unit.
  "-Xlint:option-implicit",        // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow",         // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align",            // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow",  // A local type parameter shadows a type already in scope.
  "-Ywarn-dead-code",              // Warn when dead code is identified.
  "-Ywarn-extra-implicit",         // Warn when more than one implicit parameter section is defined.
  "-Ywarn-numeric-widen",          // Warn when numerics are widened.
  "-Ywarn-unused:implicits",       // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports",         // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals",          // Warn if a local definition is unused.
  "-Ywarn-unused:params",          // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars",         // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates",        // Warn if a private member is unused.
  "-Ywarn-value-discard",          // Warn when non-Unit expression results are unused.
  // Recommended options
  "-Xlint:strict-unsealed-patmat", // warn on inexhaustive matches against unsealed traits
  // Workarounds
  "-Wconf:cat=unused&src=routes/.*:s",
  "-Wconf:cat=w-flag-dead-code&src=test/.*:s"
)

val sttpVersion = "3.10.2"
val circeVersion = "0.14.10"

val dependencies = Seq(
  "com.typesafe"                   % "config"              % "1.4.3",
  "org.typelevel"                 %% "cats-effect"         % "3.5.7",
  "com.softwaremill.sttp.client3" %% "core"                % sttpVersion,
  "com.softwaremill.sttp.client3" %% "circe"               % sttpVersion,
  "com.softwaremill.sttp.client3" %% "http4s-backend"      % sttpVersion,
  "org.http4s"                    %% "http4s-ember-client" % "0.23.30",
  "io.circe"                      %% "circe-core"          % circeVersion,
  "io.circe"                      %% "circe-generic"       % circeVersion,
  "io.circe"                      %% "circe-parser"        % circeVersion,
)

lazy val export = (project in file("export"))
  .settings(
    name := "pull-request-export",
    semanticdbEnabled := true,
    scalacOptions ++= (scalaFlags ++ paranoidFlags),
    libraryDependencies ++= dependencies
  )

lazy val creation = (project in file("creation"))
  .settings(
    name := "pull-request-creation",
    semanticdbEnabled := true,
    scalacOptions ++= (scalaFlags ++ paranoidFlags),
    libraryDependencies ++= dependencies
  )
