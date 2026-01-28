import { Component } from '@angular/core';
import { CreatePasteRequest, CreatePasteResponse, PasteService } from '../../services/paste-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-paste',
  standalone: false,
  templateUrl: './create-paste.html',
  styleUrl: './create-paste.css',
})
export class CreatePaste {

  content = '';
  ttlSeconds?: number;
  maxViews?: number;

  isSubmitting = false;
  errorMessage = '';

  constructor(
    private pasteService: PasteService,
    private router: Router
  ) { }

  submitPaste() {
    this.errorMessage = '';

    // ✅ Validation: content required
    if (!this.content || this.content.trim().length === 0) {
      this.errorMessage = 'Content is required';
      return;
    }

    const payload: CreatePasteRequest = {
      content: this.content.trim()
    };

    // ✅ Optional ttl_seconds
    if (this.ttlSeconds && this.ttlSeconds >= 1) {
      payload.ttl_seconds = this.ttlSeconds;
    }

    // ✅ Optional max_views
    if (this.maxViews && this.maxViews >= 1) {
      payload.max_views = this.maxViews;
    }

    this.isSubmitting = true;


    this.pasteService.createPaste(payload).subscribe({
      next: (res) => {
        // ✅ Redirect to view paste page
        // this.router.navigate(['/paste', res.id]);
        this.router.navigate(['/view-paste'], {
          queryParams: { id: res.id, url: res.url }
        });
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err?.error?.error || 'Failed to create paste';
      }
    });
  }

}
