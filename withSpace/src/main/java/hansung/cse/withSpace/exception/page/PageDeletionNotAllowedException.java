package hansung.cse.withSpace.exception.page;

public class PageDeletionNotAllowedException extends RuntimeException {
    public PageDeletionNotAllowedException(String message) {
        super(message);
    }
}
