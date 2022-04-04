# JetpackComposeRenderIssueExample
A small repo demonstrating potential rendering issues when using ViewTreeObserver and the OnDrawListener API

## FWIW
I'm uncertain whether or not performing subsequent draw operations in an OnDrawListener is considered 'good' practice, but this sample project demonstrates that calling `onDraw(...)` on a view containing a composable seems to break the Compose rendering unless the work to do so is placed on a `Handler`.
