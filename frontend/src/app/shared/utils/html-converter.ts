import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
  })
export class HtmlConverter {

    public getCodeSnippetAsHtml(text: string){
        var codeRegex = /(```)([^`]*)(```)/
        var escapedText = this.escapeHtml(text)
        var strSplit = escapedText.split(codeRegex);
        var finalString = strSplit.length < 2 ? text : ''
        var i = 0;

        while(i < strSplit.length-2){
        if(strSplit[i] === '```' && strSplit[i+2] === '```'){
            finalString +=  '<div class="card card-body bg-dark text-white mt-2 mb-2"><code>' + strSplit[i+1] + '</code></div>'
            i += 3
        }
        else{
            finalString += strSplit[i]
            i++
        }
        }

        return finalString
    }

    public escapeHtml(text: string){
        const escapedText: {[k: string]: string} = {
        '&': '&amp;',
        '"': '&quot;',
        '\'': '&bsol;',
        '<': '&lt;',
        '>': '&gt;',
        };
        return text.replace(/[&"'<>]/g, s => escapedText[s]);
    }
}