Set WshShell = WScript.CreateObject("WScript.Shell")
WshShell.SendKeys WScript.Arguments(0)
WshShell.SendKeys"{ENTER}"