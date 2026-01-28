import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GetPasteResponse, PasteService } from '../../services/paste-service';

@Component({
  selector: 'app-view-paste',
  standalone: false,
  templateUrl: './view-paste.html',
  styleUrl: './view-paste.css',
})
export class ViewPaste implements OnInit {

  pasteId = '';
  paste?: GetPasteResponse;
  errorMessage = '';
  createdUrl?: string;
  showCreatedUrl = false;

  constructor(
    private route: ActivatedRoute,
    private pasteService: PasteService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    // ✅ If coming from create-paste (query param)
    this.route.queryParams.subscribe(params => {
      const id = params['id'];
      if (params['url']) {
        this.createdUrl = params['url'];
        this.showCreatedUrl = true;
      } else {
        this.showCreatedUrl = false;
        this.createdUrl = undefined;
      }

      if (id) {
        this.pasteId = id;
        this.fetchPaste();
      }
    });
  }

  // ✅ Called when clicking Fetch button
  searchPaste() {
    if (!this.pasteId) {
      this.errorMessage = 'Please enter a Paste ID';
      this.paste = undefined;
      return;
    }

    // ✅ user explicitly retries
    this.errorMessage = '';
    this.paste = undefined;

    this.fetchPaste();
  }


  private fetchPaste() {
    if (!this.pasteId) {
      this.errorMessage = 'Please enter a Paste ID';
      return;
    }

    this.pasteService.getPaste(this.pasteId).subscribe({
      next: (res) => {
        this.errorMessage = '';
        this.paste = res;
        console.log(this.paste);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.paste = undefined;
        this.errorMessage =
          err.status === 404
            ? 'Paste not found or expired'
            : 'Unable to fetch paste';
        this.cdr.detectChanges();
      }

    });

  }
}
